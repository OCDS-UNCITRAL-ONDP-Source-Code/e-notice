package com.procurement.notice.model.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@Table("notice_package")
public class PackageEntity {

    @PrimaryKeyColumn(name = "cp_id", type = PrimaryKeyType.PARTITIONED)
    private String cpId;

    @PrimaryKeyColumn(name = "package_date", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime packageDate;

    @Column(value = "json_data")
    private String jsonData;
}