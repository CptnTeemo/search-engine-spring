package searchengine.dto.response;

import lombok.Value;
import searchengine.dto.statistic.Statistics;

@Value
public class StatisticResponse {
    boolean result;
    Statistics statistics;
}
