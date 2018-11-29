import os
import pandas as pd
import numpy as np
import requests
import matplotlib.pyplot as plt


def load_deficit_data():
    if not os.path.exists('deficit.csv'):
        r = requests.get('https://www.usgovernmentdebt.us/rev/usgs_downchart_csv.php?year=1970_2020&state=US&units=p&view=2&fy=fy20&chart=G0-fed&stack=1&title=Fifty%20Years%20Of%20Federal%20Deficits%20As%20Pct%20GDP&local=s&thing=')
        if not r.ok:
            raise RuntimeError('Failed to load deficit data: {}'.format(r.status_code))
        with open('deficit.csv', 'w') as data_file:
            data_file.write(r.text)
            data_file.flush()
    return pd.read_csv('deficit.csv',
                       skiprows=1,
                       header=0,
                       index_col=0,
                       skipfooter=7,
                       engine='python',
                       error_bad_lines=False,
                       warn_bad_lines=False) \
        .drop(columns=['GDP-US $ billion nominal', 'Population-US million', 'Unnamed: 4']) \
        .rename({'Federal Deficit-fed percent GDP': '% GDP'}, axis='columns') \
        .loc[:'2010']  # Drop all the data after 2010 because the original chart was made in 2011


def load_presidents_data():
    if not os.path.exists('presidents.csv'):
        r = requests.get('https://gist.githubusercontent.com/namuol/2657233/raw/74135b2637e624848c163759be9cd14ae33f5153/presidents.csv')
        if not r.ok:
            raise RuntimeError('Falied to download presidents data: {}'.format(r.status_code))
        with open('presidents.csv', 'w') as data_file:
            data_file.write(r.text)
            data_file.flush()
    presidents = pd.read_csv('presidents.csv')
    presidents.columns = np.char.strip(presidents.columns.values.astype(str))
    presidents.set_index('President', inplace=True)
    presidents.loc['Barack Obama', 'Left office'] = '2017-01-20'
    presidents = presidents.loc[:, np.isin(presidents.columns, ['Took office', 'Left office'])] \
        .apply(pd.to_datetime, errors='coerce')
    # Adjust for the federal fiscal year that starts on 10/01 and ends on 09/30.
    # The fiscal year is designated by the calendar year in which it ends
    # End - the first fiscal year the president does not influence on
    presidents['End'] = presidents['Left office'].apply(lambda x: x.year + 1 if x.month < 10 else x.year + 2)
    # Start - the first fiscal year the president influences on
    presidents['Start'] = presidents['End'].shift()
    presidents = presidents.loc['John F. Kennedy':, presidents.columns[[0, 1, 3, 2]]]
    presidents['Start'] = presidents['Start'].astype(int)
    return presidents


def get_spaced_colors(n):
    max_value = 255 ** 3
    interval = int(max_value / n)
    colors = [hex(I)[2:].zfill(6) for I in range(0, max_value, interval)]
    return [(int(i[:2], 16), int(i[2:4], 16), int(i[4:], 16)) for i in colors]


def pick_colors_for_presidents(m):
    c = list(map(lambda x: (x[0] / 255, x[1] / 255, x[2] / 255), get_spaced_colors(len(m['President'].unique()))))
    return dict(zip(m['President'].unique(), c))


deficit = load_deficit_data()
presidents = load_presidents_data()
m = pd.merge(deficit.loc[:presidents['End'].max() - 1],
             presidents.reset_index().set_index('Start').drop(columns=['Took office', 'Left office', 'End']),
             how='left', left_index=True, right_index=True) \
    .fillna(method='ffill')
print(m)

colors = pick_colors_for_presidents(m)
fig, (ax1, ax2) = plt.subplots(nrows=2, ncols=1)
fig.canvas.set_window_title('Budget deficits')
fig.suptitle('US Budget deficits as a % of GDP in 1970-2010, '
             'by administration, with adjustments for 10/01 - 09/30 fiscal year')
# fig.set_size_inches(800, 600)

for president, df in m.groupby('President', sort=False):
    ax1.bar(df.index, -1 * df['% GDP'], label=president.split()[-1], color=colors[president])
ax1.set_xticks([t for t in m.index if t % 5 == 0])
ax1.set_ylabel('Deficits as % of GDP')
ax1.yaxis.tick_right()
ax1.legend(loc=3)

avg = m.groupby('President', sort=False).mean()['% GDP']
ax2.bar(avg.index, -1 * avg, color='grey')
ax2.set_ylabel('Average deficits as % of GDP')
for p in ax2.patches:
    ax2.annotate('{:0.2f}'.format(p.get_height()),
                 (p.get_x() + p.get_width() / 2, p.get_height() * 0.9),
                 ha='center', va='center')
ax2.yaxis.tick_right()

plt.show()
