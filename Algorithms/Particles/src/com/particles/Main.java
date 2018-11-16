package com.particles;

public class Main {

    public static void main(String[] args) {
        Particle[] particles = new Particle[100];
        for (int i = 0; i != 100; ++i) {
            particles[i] = new Particle();
        }

        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(Double.POSITIVE_INFINITY);
    }
}
