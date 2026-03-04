//start L3 Decoratoe
package org.pollub.branch.config;

import org.pollub.branch.service.BranchService;
import org.pollub.branch.service.BranchServiceLoggingDecorator;
import org.pollub.branch.service.IBranchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for the BranchService logging decorator.
 */
@Configuration
public class BranchServiceConfig {
    @Bean
    @Primary
    public IBranchService branchService(@Qualifier("baseBranchService") BranchService branchService) {
        return new BranchServiceLoggingDecorator(branchService);
    }
}
//end L3 Decorator Config
