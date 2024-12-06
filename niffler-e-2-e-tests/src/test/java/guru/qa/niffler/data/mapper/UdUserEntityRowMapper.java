package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.rest.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UdUserEntityRowMapper implements RowMapper<UdUserEntity> {

  public static final UdUserEntityRowMapper instance = new UdUserEntityRowMapper();

  private UdUserEntityRowMapper() {
  }

  @Nonnull
  @Override
  public UdUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UdUserEntity result = new UdUserEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    result.setFirstname(rs.getString("firstname"));
    result.setSurname(rs.getString("surname"));
    result.setFullname(rs.getString("full_name"));
    result.setPhoto(rs.getBytes("photo"));
    result.setPhotoSmall(rs.getBytes("photo_small"));
    return result;
  }
}
