package searchengine.dto.statistic;

import lombok.Value;
import searchengine.entity.Status;

import java.util.Date;

@Value
public class Detailed {
    String url;
    String name;
    Status status;
    Date statusTime;
    String error;
    Long pages;
    Long lemmas;
}
