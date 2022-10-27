package searchengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.response.StatisticResponse;
import searchengine.service.StatisticService;

@RestController
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/statistics")
    public ResponseEntity<Object> getStatistic() {
        var statistic = statisticService.getStatistic();
        return new ResponseEntity<>(new StatisticResponse(true, statistic), HttpStatus.OK);
    }
}
