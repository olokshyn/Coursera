Budget deficits game: who is the worst president?

Americans are worried about the politics and the national debt, so the intersection of this two topics is a mainstream direction for many journalists. Despite the president`s influence on the budget is limited by the Congress and there were many unforeseen circumstances that increased expenditures drastically, the articles revealing the most "spending" presidents keep appearing.

There is an article of this genre, claiming that there were no bigger budget deficits as under President Obama`s administration for the last 50 years. The article has been posted on myheritage.org - the online home for The Heritage Foundation: https://www.myheritage.org/news/federal-deficit-largest-in-fifty-years-under-obama/ .The similar post has been made on The Daily Signal - a journalism news site that is also run by The Heritage Foundation: https://www.dailysignal.com/2012/01/01/chart-of-the-week-u-s-presidents-ranked-by-budget-deficits/.

The Heritage Foundation is a conservative public policy think tank. Thus, the intended audience of the articles are people with conservatives views, possibly republicans, of all social levels (the article does not require any prior knowledge in economics or politics, neither it requires specific professional interests).

The most misleading component of the visual is that the presidents had a different amount of years at the office, but the deficits values displayed are just average among all the years. Another misleading component of the visual is not mentioning that sometimes during the first year at the office the president does not influence the budget at all: the budget is approved before the 1st of October for the whole fiscal year, starting from the 1st of October till 30th of September. Thus, the president that took office after the 1st of October cannot influence the budget until the end of the fiscal year. The mechanism used in both obfuscations is hiding relevant data.

To demonstrate how hiding relevant data misleads in this case, I have built another chart without averaging and taking into account federal fiscal years.

The following chart takes into account the federal fiscal year: the president is considered responsible for the budget at the year iff the president was at the office before the 1st of October of the previous year. For example, Barak Obama took office on January 20, 2009. The budget for the 2009 fiscal year was approved before the 1st of October, 2008. Thus, Obama is not considered responsible for the 2009 year budget because that budget was approved under the Bush administration.

Note that the chart displays budget deficits from 1970 to 2010 by the president. The years after 2010 are not considered because the original chart by heritage.org dates back to 2011 and they may not have the data for 2011 onwards.

View the chart here: https://github.com/olokshyn/Coursera/blob/master/AppliedPythonDS/Deficits/Budget_deficits.png 
You can get the script used to build the chart here: https://github.com/olokshyn/Coursera/blob/master/AppliedPythonDS/Deficits/deficit.py

As you can see on the first chart, the biggest budget deficits were under the budget approved by the Bush administration: 9.80% in 2009, while the budget deficits under the budget approved by the Obama administration in 2010 were 8.56%. Thus, Obama even managed to improve the budget deficits after Bush, not worsen it. But the averaging of the deficits shows us a completely different picture as you can see on the second chart.

This was an example of how averaging could hide relevant data and obscure the real situation, while the visual still looks trustworthy.

Besides the issues described, the chart from heritage.org compares the average of deficits of previous presidents to Obama`s estimated deficits. Moreover, the estimate is likely to be made from years 2009 and 2010, while the budget for 2009 year was approved by the Bush administration.
