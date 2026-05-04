package com.empcloud.empmonitor.data.local.Location;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.empcloud.empmonitor.data.remote.response.locationRoom.LocationEntity;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class LocationDao_Impl implements LocationDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<LocationEntity> __insertAdapterOfLocationEntity;

  public LocationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfLocationEntity = new EntityInsertAdapter<LocationEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `location_table` (`id`,`latitude`,`longitude`,`timeStamp`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final LocationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getLatitude());
        statement.bindDouble(3, entity.getLongitude());
        if (entity.getTimeStamp() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getTimeStamp());
        }
      }
    };
  }

  @Override
  public Object insert(final LocationEntity location,
      final Continuation<? super Unit> $completion) {
    if (location == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfLocationEntity.insert(_connection, location);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object getAllLocations(final Continuation<? super List<LocationEntity>> $completion) {
    final String _sql = "SELECT * FROM location_table";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfLatitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "latitude");
        final int _columnIndexOfLongitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "longitude");
        final int _columnIndexOfTimeStamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "timeStamp");
        final List<LocationEntity> _result = new ArrayList<LocationEntity>();
        while (_stmt.step()) {
          final LocationEntity _item;
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          final double _tmpLatitude;
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude);
          final double _tmpLongitude;
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude);
          final String _tmpTimeStamp;
          if (_stmt.isNull(_columnIndexOfTimeStamp)) {
            _tmpTimeStamp = null;
          } else {
            _tmpTimeStamp = _stmt.getText(_columnIndexOfTimeStamp);
          }
          _item = new LocationEntity(_tmpId,_tmpLatitude,_tmpLongitude,_tmpTimeStamp);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final int id, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM location_table WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
