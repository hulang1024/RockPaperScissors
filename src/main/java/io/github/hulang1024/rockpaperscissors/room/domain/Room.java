package io.github.hulang1024.rockpaperscissors.room.domain;

import io.github.hulang1024.rockpaperscissors.match.domain.Match;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Room {
    @NonNull
    private String id;
    @NonNull
    private RoomMatchSettings matchSettings;
    @NonNull
    private List<Object> participantIdIds = new ArrayList<>();
    @NonNull
    private Map<Object, Boolean> participantToReadyMap = new HashMap<>();

    public Room(@NonNull String id, RoomMatchSettings matchSettings) {
        this.id = id;
        this.matchSettings = matchSettings;
    }

    public void join(@NonNull Object participantId) {
        if (participantIdIds.contains(participantId)) {
            return;
        }
        if (participantIdIds.size() == 2) {
            throw new IllegalArgumentException("已达到参与者最大数量");
        }
        participantIdIds.add(participantId);
        if (participantIdIds.size() == 1) {
            ready(participantId);
        } else {
            unready(participantId);
        }
    }

    public void ready(@NonNull Object participantId) {
        if (!participantIdIds.contains(participantId)) {
            throw new IllegalArgumentException("未加入");
        }
        participantToReadyMap.put(participantId, true);
    }

    public void unready(@NonNull Object participantId) {
        if (!participantIdIds.contains(participantId)) {
            throw new IllegalArgumentException("未加入");
        }
        participantToReadyMap.put(participantId, false);
    }

    @NonNull
    public Match createMatch() {
        return Match.ready(this.id, participantIdIds.get(0), participantIdIds.get(1), matchSettings.getRoundNumber());
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public RoomMatchSettings getMatchSettings() {
        return matchSettings;
    }

    @NonNull
    public List<Object> getParticipantIdIds() {
        return new ArrayList<>(participantIdIds);
    }

    @NonNull
    public Map<Object, Boolean> getParticipantToReadyMap() {
        return new HashMap<>(participantToReadyMap);
    }
}
