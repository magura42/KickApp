package com.example.mharrer.kickapp.service;

import com.example.mharrer.kickapp.model.Training;
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
public class TrainingServiceClient {

    private static TrainingServiceClient trainingServiceClient;

    private List<Training> trainings = new ArrayList<Training>();

    public static TrainingServiceClient getInstance() {
        if (trainingServiceClient == null) {
            trainingServiceClient = new TrainingServiceClient();
        }
        return trainingServiceClient;
    }

    private TrainingServiceClient() {
        Training training1 = new Training("1.2.2014", "training1", new Long(1));
        Training training2 = new Training("13.5.2014", "training2", new Long(2));
        Training training3 = new Training("21.9.2014", "training3", new Long(3));

        training1.setCoach(CoachServiceClient.getInstance().getCoach(1).get());
        training2.setCoach(CoachServiceClient.getInstance().getCoach(2).get());
        training3.setCoach(CoachServiceClient.getInstance().getCoach(3).get());

        training1.setTeam(TeamServiceClient.getInstance().getTeam(1).get());
        training2.setTeam(TeamServiceClient.getInstance().getTeam(2).get());
        training3.setTeam(TeamServiceClient.getInstance().getTeam(3).get());

        trainings.add(training1);
        trainings.add(training2);
        trainings.add(training3);

    }


    public List<Training> getAllTrainings() {
        return trainings;
    }

    public Optional<Training> getTraining(final long id) {
        return FluentIterable.from(trainings).filter(new Predicate<Training>() {
            @Override
            public boolean apply(Training input) {
                if (input.getId().longValue() == id) {
                    return true;
                }
                return false;
            }
        }).first();
    }

    public void updateTraining(Training training) {
        if (training.getId() > 0) {
            Optional<Training> oldTraining = getTraining(training.getId());
            if (oldTraining.isPresent()) {
                trainings.remove(oldTraining.get());
            }
            trainings.add(training);
        }
    }

    public void deleteTraining(Training training) {
        if (training.getId() > 0) {
            Optional<Training> oldTraining = getTraining(training.getId());
            if (oldTraining.isPresent()) {
                trainings.remove(oldTraining.get());
            }
        }
    }

    public void createNewTraining(Training training) {
        Training maxId = Collections.max(trainings, new Comparator<Training>() {
            @Override
            public int compare(Training o1, Training o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        training.setId(maxId.getId()+1);
        trainings.add(training);
    }
}
