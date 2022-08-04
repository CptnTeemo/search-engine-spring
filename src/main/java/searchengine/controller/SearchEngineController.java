//package controller;
//
//import dto.PageDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import service.impl.SearchEngineServiceImpl;
//
//import java.util.List;
//
//@RestController
//public class SearchEngineController {
//
//    @Autowired
//    private SearchEngineServiceImpl searchEngineService;
//
//    @GetMapping(value = "/")
//    public List<PageDto> getPagesList() {
//        return searchEngineService.getPagesDto();
//    }
//}
