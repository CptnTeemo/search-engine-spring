//package service.impl;
//
//import dto.PageDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import repository.SearchEngineRepository;
//import service.SearchEngineService;
//import utils.MappingUtils;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class SearchEngineServiceImpl implements SearchEngineService {
//
//    @Autowired
//    private SearchEngineRepository searchEngineRepository;
//    private MappingUtils mappingUtils;
//
////    @Override
////    @Transactional(readOnly = true)
////    public List<PageDto> getPagesDto() {
////        List<PageDto> pageDtoList;
////        pageDtoList = searchEngineRepository.findAll().stream()
////                .map(e -> MappingUtils.pageEntityToPageDto(e))
////                .collect(Collectors.toList());
////        return pageDtoList;
////    }
//
//}
