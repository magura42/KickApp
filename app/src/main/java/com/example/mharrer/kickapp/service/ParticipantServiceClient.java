package com.example.mharrer.kickapp.service;

import com.example.mharrer.kickapp.model.Participant;
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
public class ParticipantServiceClient {

    private final List<Participant> participants = new ArrayList<Participant>();

    private static ParticipantServiceClient instance;

    public static ParticipantServiceClient getInstance() {
        if (instance == null) {
            instance = new ParticipantServiceClient();
        }
        return instance;
    }

    private ParticipantServiceClient() {
        participants.add(new Participant("vorname0", "nachname0", "0176/10351031", 1));
        participants.add(new Participant("vorname1", "nachname1", "0176/10351031", 2));
        participants.add(new Participant("vorname2", "nachname2", "0176/10351031", 3));
        participants.add(new Participant("vorname3", "nachname3", "0176/10351031", 4));
        participants.add(new Participant("vorname4", "nachname4", "0176/10351031", 5));
        participants.add(new Participant("vorname5", "nachname5", "0176/10351031", 6));
        participants.add(new Participant("vorname6", "nachname6", "0176/10351031", 7));
        participants.add(new Participant("vorname7", "nachname7", "0176/10351031", 8));
        participants.add(new Participant("vorname8", "nachname8", "0176/10351031", 9));
        participants.add(new Participant("vorname9", "nachname9", "0176/10351031", 10));
        participants.add(new Participant("vorname10", "nachname10", "0176/10351031", 11));
        participants.add(new Participant("vorname11", "nachname11", "0176/10351031", 12));
        participants.add(new Participant("vorname12", "nachname12", "0176/10351031", 13));
        participants.add(new Participant("vorname13", "nachname13", "0176/10351031", 14));
        participants.add(new Participant("vorname14", "nachname14", "0176/10351031", 15));
        participants.add(new Participant("vorname15", "nachname15", "0176/10351031", 15));
        participants.add(new Participant("vorname16", "nachname16", "0176/10351031", 16));
        participants.add(new Participant("vorname17", "nachname17", "0176/10351031", 17));
        participants.add(new Participant("vorname18", "nachname18", "0176/10351031", 18));
        participants.add(new Participant("vorname19", "nachname19", "0176/10351031", 19));
    }


    public List<Participant> getAllParticipants() {
        return participants;
    }

    public Optional<Participant> getParticipant(final long id) {
        return FluentIterable.from(participants).filter(new Predicate<Participant>() {
            @Override
            public boolean apply(Participant input) {
                if (input.getId().longValue() == id) {
                    return true;
                }
                return false;
            }
        }).first();
    }

    public void updateParticipant(Participant participant) {
        if (participant.getId() > 0) {
            Optional<Participant> oldParticipant = getParticipant(participant.getId());
            if (oldParticipant.isPresent()) {
                participants.remove(oldParticipant.get());
            }
            participants.add(participant);
        }
    }

    public void deleteParticipant(Participant participant) {
        if (participant.getId() > 0) {
            Optional<Participant> oldParticipant = getParticipant(participant.getId());
            if (oldParticipant.isPresent()) {
                participants.remove(oldParticipant.get());
            }
        }
    }

    public void createNewParticipant(Participant participant) {
        Participant maxId = Collections.max(participants, new Comparator<Participant>() {
            @Override
            public int compare(Participant o1, Participant o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        participant.setId(maxId.getId()+1);
        participants.add(participant);
    }
}
