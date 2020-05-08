package wooteco.subway.admin.domain;

import static java.util.stream.Collectors.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Line {
    @Id
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private String bgColor;
    private List<LineStation> stations = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Line() {
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime, String bgColor) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.bgColor = bgColor;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime, String bgColor) {
        this(null, name, startTime, endTime, intervalTime, bgColor);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public String getBgColor() {
        return bgColor;
    }

    public List<LineStation> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(Line line) {
        if (line.getName() != null) {
            this.name = line.getName();
        }
        if (line.getStartTime() != null) {
            this.startTime = line.getStartTime();
        }
        if (line.getEndTime() != null) {
            this.endTime = line.getEndTime();
        }
        if (line.getIntervalTime() != 0) {
            this.intervalTime = line.getIntervalTime();
        }
        if (line.getBgColor() != null) {
            this.bgColor = line.getBgColor();
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void addLineStation(LineStation lineStation) {
        // TODO: 구현
        if (Objects.isNull(lineStation.getPreStationId())) {
            stations.add(0, lineStation);
            return;
        }

        Optional<LineStation> next = next(lineStation);
        if (next.isPresent()) {
            LineStation realNext = next.get();
            realNext.updatePreLineStation(lineStation.getStationId());
            stations.add(stations.indexOf(realNext), lineStation);
            return;
        }

        stations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        Optional<LineStation> station = stations.stream()
            .filter(lineStation -> lineStation.getStationId().equals(stationId))
            .findAny();

        station.ifPresent(lineStation -> stations.remove(lineStation));
    }

    public List<Long> getLineStationsId() {
        return stations.stream()
            .map(LineStation::getStationId)
            .collect(toList());
    }

    //입력한 lineStation의 이전 노드 위치를 반환
    private LineStation prev(LineStation lineStation) {
        return stations.stream()
            .filter(station-> station.getStationId()
                .equals(lineStation.getPreStationId()))
            .findAny()
            .orElse(null);
    }

    //입력한 lineStation의 다음 노드 위치를 반환
    private Optional<LineStation> next(LineStation lineStation) {
        return stations.stream()
            .filter(station-> Objects.nonNull(station.getPreStationId()))
            .filter(station-> station.getPreStationId()
                .equals(lineStation.getPreStationId()))
            .findAny();
    }
}
