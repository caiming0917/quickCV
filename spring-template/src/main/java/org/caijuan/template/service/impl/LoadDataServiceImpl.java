package org.caijuan.template.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.caijuan.template.service.LoadDataService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoadDataServiceImpl implements LoadDataService {

    @Override
    public void load() {
        log.info("数据预热...");
    }
}
