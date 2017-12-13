package com.procurement.notice.service;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public interface ReleaseService {

    ResponseDto saveTwineRecordRelease( RequestDto data);

    ResponseDto saveRecordRelease(String cpId, RequestDto data);

}
