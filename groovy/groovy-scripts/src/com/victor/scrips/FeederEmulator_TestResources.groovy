import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.Resource

String aTestsPath    =    "file:\\development\\projects\\jboss_vcloud_trunk\\feeder-emulator\\src\\main\\resources\\xml"
ClassLoader.getSystemClassLoader().addURL(aTestsPath.toURL())

PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver()
Resource[] resources = resolver.getResources(aTestsPath)

resources.each{ Resource aResource ->
    println "Resource Name: ${aResource.filename}"
    
    aResource.getFile().listFiles().each{ File aFolder ->
        if (aFolder.name == "ALE") {
            println "\t${aFolder}"
            
            aFolder.listFiles().each{ File aTestFolder ->
                println "\t\t${aTestFolder}"
            }
        }
    }
}