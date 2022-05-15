package com.ruoxi.congestiontaxcalculator.repositories;

import com.ruoxi.congestiontaxcalculator.repositories.fee.TollFeeTimedRule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TollFeeTimedRuleRepository extends MongoRepository<TollFeeTimedRule, String> {}
