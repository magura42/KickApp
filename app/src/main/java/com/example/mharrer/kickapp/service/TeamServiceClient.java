package com.example.mharrer.kickapp.service;

import com.example.mharrer.kickapp.model.Participant;
import com.example.mharrer.kickapp.model.Team;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mharrer on 12.11.14.
 */
public class TeamServiceClient {

    private static TeamServiceClient teamServiceClient;
    private final List<Team> teams = new ArrayList<Team>();

    public static TeamServiceClient getInstance() {
        if (teamServiceClient == null) {
            teamServiceClient = new TeamServiceClient();
        }
        return teamServiceClient;
    }

    private TeamServiceClient() {
        List<Participant> allParticipants = ParticipantServiceClient.getInstance().getAllParticipants();
        Team team1 = new Team("F-Jugend", 1l);
        team1.addTeammember(allParticipants.get(0));
        team1.addTeammember(allParticipants.get(1));
        team1.addTeammember(allParticipants.get(2));
        team1.addTeammember(allParticipants.get(3));
        teams.add(team1);

        Team team2 = new Team("E-Jugend", 2l);
        team2.addTeammember(allParticipants.get(4));
        team2.addTeammember(allParticipants.get(5));
        team2.addTeammember(allParticipants.get(6));
        team2.addTeammember(allParticipants.get(7));
        team2.addTeammember(allParticipants.get(8));
        teams.add(team2);

        Team team3 = new Team("D-Jugend", 3l);
        team2.addTeammember(allParticipants.get(9));
        team2.addTeammember(allParticipants.get(10));
        teams.add(team3);
    }

    public List<Team> getAllTeams() {
        return teams;
    }

    public Optional<Team> getTeam(final long id) {
        return FluentIterable.from(teams).filter(new Predicate<Team>() {
            @Override
            public boolean apply(Team input) {
                if (input.getId().longValue() == id) {
                    return true;
                }
                return false;
            }
        }).first();
    }

    public void updateTeam(Team team) {
        if (team.getId() > 0) {
            Optional<Team> oldTeam = getTeam(team.getId());
            if (oldTeam.isPresent()) {
                teams.remove(oldTeam.get());
            }
            teams.add(team);
        }
    }

    public void deleteTeam(Team team) {
        if (team.getId() > 0) {
            Optional<Team> oldTeam = getTeam(team.getId());
            if (oldTeam.isPresent()) {
                teams.remove(oldTeam.get());
            }
        }
    }

    public void createNewTeam(Team team) {
        Team maxId = Collections.max(teams, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        team.setId(maxId.getId() + 1);
        teams.add(team);
    }

    public List<Participant> getFreeParticipants() {
        List<Team> allTeams = getAllTeams();
        ImmutableList<Participant> allTeamMembers = FluentIterable.from(allTeams).transformAndConcat(new Function<Team, List<Participant>>() {

            @Override
            public List<Participant> apply(Team input) {
                return input.getTeammembers();
            }
        }).toList();

        List<Participant> allParticipants = ParticipantServiceClient.getInstance().getAllParticipants();
        return ListUtils.subtract(allParticipants, allTeamMembers);
    }
}