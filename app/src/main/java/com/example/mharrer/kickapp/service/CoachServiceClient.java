package com.example.mharrer.kickapp.service;

import com.example.mharrer.kickapp.model.Coach;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mharrer on 10.11.14.
 */
public class CoachServiceClient {

    private final List<Coach> coaches = new ArrayList<Coach>();

    private static CoachServiceClient instance;

    public static CoachServiceClient getInstance() {
        if (instance == null) {
            instance = new CoachServiceClient();
        }
        return instance;
    }

    private CoachServiceClient() {
        Coach c1  = new Coach("Franz", "Beckenbauer", new Long(1));
        Coach c2  = new Coach("Rudi", "Völler", new Long(2));
        Coach c3  = new Coach("Hubert", "Vogts", new Long(3));
        coaches.add(c1);
        coaches.add(c2);
        coaches.add(c3);
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public Optional<Coach> getCoach(final long id) {
        return FluentIterable.from(coaches).filter(new Predicate<Coach>() {
            @Override
            public boolean apply(Coach input) {
                if (input.getId().longValue() == id) {
                    return true;
                }
                return false;
            }
        }).first();
    }

    public void updateCoach(Coach coach) {
        if (coach.getId() > 0) {
            Optional<Coach> oldCoach = getCoach(coach.getId());
            if (oldCoach.isPresent()) {
                coaches.remove(oldCoach.get());
            }
            coaches.add(coach);
        }
    }

    public void deleteCoach(Coach coach) {
        if (coach.getId() > 0) {
            Optional<Coach> oldCoach = getCoach(coach.getId());
            if (oldCoach.isPresent()) {
                coaches.remove(oldCoach.get());
            }
        }
    }

    public void createNewCoach(Coach coach) {
        Coach maxId = Collections.max(coaches, new Comparator<Coach>() {
            @Override
            public int compare(Coach o1, Coach o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        coach.setId(maxId.getId()+1);
        coaches.add(coach);
    }
}
