package ningenme.net.sample.mapper

import ningenme.net.sample.mapper.dto.UserDto
import org.dbunit.database.DatabaseConfig
import org.dbunit.database.DatabaseConnection
import org.dbunit.database.IDatabaseConnection
import org.dbunit.dataset.filter.DefaultColumnFilter
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder
import org.dbunit.ext.mysql.MySqlMetadataHandler
import org.dbunit.operation.DatabaseOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

import static org.dbunit.Assertion.assertEquals

@SpringBootTest
class UserMysqlMapperSpec extends Specification {

    private final static String FILE_PATH = "src/test/groovy/ningenme/net/sample/mapper"

    @Autowired
    JdbcTemplate jdbcTemplate

    @Autowired
    UserMysqlMapper userMysqlMapper

    IDatabaseConnection iDatabaseConnection

    def setup() {
        iDatabaseConnection = new DatabaseConnection(jdbcTemplate.getDataSource().getConnection(),"sample",false);
        iDatabaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
        DatabaseOperation.CLEAN_INSERT.execute(
                iDatabaseConnection,
                new FlatXmlDataSetBuilder().build(new File(FILE_PATH + "/setup.xml"))
        )
    }

    def "insert_新規挿入が出来る" (){
        when:
        def user3 = new UserDto();
        {
            user3.setId(3)
            user3.setName("user3")
        }
        def user4 = new UserDto();
        {
            user4.setId(4)
            user4.setName("user4")
        }
        userMysqlMapper.insert([user3, user4]);

        then:
        def actual = DefaultColumnFilter.excludedColumnsTable(
                iDatabaseConnection.createDataSet().getTable("users"),
                new String[]{"deleted_time","created_time","updated_time"}
        )

        def expect = DefaultColumnFilter.excludedColumnsTable(
                new FlatXmlDataSetBuilder().build(new File(FILE_PATH + "/expect.xml")).getTable("users"),
                new String[]{"deleted_time","created_time","updated_time"}
        )
        assertEquals(actual,expect)
        noExceptionThrown()
    }

    def "insert_重複でエラーになる" (){
        when:
        def user1 = new UserDto();
        {
            user1.setId(1)
            user1.setName("user1")
        }
        userMysqlMapper.insert([user1]);

        then:
        thrown(DuplicateKeyException)
    }

    def cleanup() {
        iDatabaseConnection.close()
    }
}
