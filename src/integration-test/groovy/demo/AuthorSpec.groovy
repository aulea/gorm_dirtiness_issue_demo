package demo

import ch.qos.logback.classic.Level
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

@Rollback
@Integration
class AuthorSpec extends Specification {
        
    def "demonstrate that GORM tries to save also formula fields"() {
        setup: 'Track Hibernate warnings'
        LogMessagesTracker hibernateWarnings = LogMessagesTracker.trackLogMessagesFor('org.hibernate', Level.WARN)
        
        and: 'we will have Author'
        Author author = new Author()
        author.firstname = 'First'
        author.lastname = 'Last'
        author.save(flush: true)
        
        when: 'marking author dirty as whole'
        author.markDirty()
        author.save(flush: true)
        
        then: "we should not have any warnings"
        hibernateWarnings.events.size() == 0
    }
}
