package demo

import grails.compiler.GrailsCompileStatic
import grails.gorm.hibernate.mapping.MappingBuilder

@GrailsCompileStatic
class Author {

    String firstname
    String lastname
    
    // formula - "lastname, firstname"
    String name
    
    static constraints = {
    }

    static mapping = MappingBuilder.orm {
        property 'name', [formula: "lastname || ', ' || firstname"]
    }
}
