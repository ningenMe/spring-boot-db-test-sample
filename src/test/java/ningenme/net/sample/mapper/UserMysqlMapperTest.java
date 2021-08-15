package ningenme.net.sample.mapper;

import ningenme.net.sample.mapper.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

import static org.dbunit.Assertion.assertEquals;

@SpringBootTest
class UserMysqlMapperTest {

  private final static String FILE_PATH = "src/test/java/ningenme/net/sample/mapper";

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  UserMysqlMapper userMysqlMapper;

  IDatabaseConnection iDatabaseConnection;

  @BeforeEach
  void beforeEach() throws SQLException, MalformedURLException, DatabaseUnitException {
    iDatabaseConnection = new DatabaseConnection(jdbcTemplate.getDataSource().getConnection(), "sample", false);
    iDatabaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
    DatabaseOperation.CLEAN_INSERT.execute(
            iDatabaseConnection,
            new FlatXmlDataSetBuilder().build(new File(FILE_PATH + "/setup.xml")));
  }

  @Test
  void insert_新規挿入が出来る() throws SQLException, DatabaseUnitException, MalformedURLException {
    //when
    UserDto user3 = new UserDto();
    {
      user3.setId(3);
      user3.setName("user3");
    }
    UserDto user4 = new UserDto();
    {
      user4.setId(4);
      user4.setName("user4");
    }
    userMysqlMapper.insert(List.of(user3, user4));


    //then
    ITable actual = DefaultColumnFilter.excludedColumnsTable(
            iDatabaseConnection.createDataSet().getTable("users"),
            new String[]{"deleted_time","created_time","updated_time"});

    ITable expect = DefaultColumnFilter.excludedColumnsTable(
            new FlatXmlDataSetBuilder().build(new File(FILE_PATH + "/expect.xml")).getTable("users"),
            new String[]{"deleted_time","created_time","updated_time"});

    assertEquals(actual,expect);
  }

  @Test
  void insert_重複でエラーになる(){
    //when
    UserDto user1 = new UserDto();
    {
      user1.setId(1);
      user1.setName("user1");
    }

    //then
    Assertions.assertThatThrownBy(() -> userMysqlMapper.insert(List.of(user1))).isInstanceOf(DuplicateKeyException.class);
  }

  @AfterEach
  void afterEach() throws SQLException {
    iDatabaseConnection.close();
  }
}