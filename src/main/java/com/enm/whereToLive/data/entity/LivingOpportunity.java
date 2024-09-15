package com.enm.whereToLive.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "test2")
public class LivingOpportunity {

    @Id
    private LivingOpportunityId id; // 복합 키를 위한 ID 클래스

    @DynamoDBHashKey(attributeName = "test")
    public String getTest() {
        return this.id.getTest();
    }

    public void setTest(String test) {
        this.id.setTest(test);
    }

    @DynamoDBRangeKey(attributeName = "tests")
    public String getTests() {
        return this.id.getTests();
    }

    public void setTests(String tests) {
        this.id.setTests(tests);
    }
}