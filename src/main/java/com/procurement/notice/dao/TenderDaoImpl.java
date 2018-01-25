package com.procurement.notice.dao;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.procurement.notice.model.entity.TenderEntity;
import org.springframework.stereotype.Service;

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;

@Service
public class TenderDaoImpl implements TenderDao {

    private static final String TENDER_TABLE = "notice_release";
    private static final String TENDER_COMPILED_TABLE = "notice_compiled_release";
    private static final String TENDER_OFFSET_TABLE = "notice_offset";
    private static final String CP_ID = "cp_id";
    private static final String OC_ID = "oc_id";
    private static final String RELEASE_DATE = "release_date";
    private static final String RELEASE_ID = "release_id";
    private static final String STAGE = "stage";
    private static final String JSON_DATA = "json_data";

    private final Session session;

    public TenderDaoImpl(final Session session) {
        this.session = session;
    }

    @Override
    public void saveTender(final TenderEntity releaseEntity) {
        final Insert insert = insertInto(TENDER_TABLE);
        insert
                .value(CP_ID, releaseEntity.getCpId())
                .value(OC_ID, releaseEntity.getOcId())
                .value(RELEASE_DATE, releaseEntity.getReleaseDate())
                .value(RELEASE_ID, releaseEntity.getReleaseId())
                .value(STAGE, releaseEntity.getStage())
                .value(JSON_DATA, releaseEntity.getJsonData());

        final Insert insertCompiled = insertInto(TENDER_COMPILED_TABLE);
        insertCompiled
                .value(CP_ID, releaseEntity.getCpId())
                .value(OC_ID, releaseEntity.getOcId())
                .value(RELEASE_DATE, releaseEntity.getReleaseDate())
                .value(RELEASE_ID, releaseEntity.getReleaseId())
                .value(STAGE, releaseEntity.getStage())
                .value(JSON_DATA, releaseEntity.getJsonData());

        final Insert insertOffset = insertInto(TENDER_OFFSET_TABLE);
        insertOffset
                .value(CP_ID, releaseEntity.getCpId())
                .value(RELEASE_DATE, releaseEntity.getReleaseDate());

        final Batch batch = QueryBuilder.batch(insert, insertCompiled, insertOffset);
        session.execute(batch);
    }
}