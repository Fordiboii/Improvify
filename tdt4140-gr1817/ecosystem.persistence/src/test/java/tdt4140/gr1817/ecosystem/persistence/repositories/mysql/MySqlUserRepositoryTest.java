package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllUsersSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

public class MySqlUserRepositoryTest {

    @Rule
    public HsqldbRule hsqldb = new HsqldbRule();

    @Test
    public void shouldAddUser() throws Exception {
        // Given
        final Connection connectionSpy = Mockito.spy(hsqldb.getConnection()); // Will be closed
        MySqlUserRepository repository = new MySqlUserRepository(() -> connectionSpy);
        final Date birthDate = new GregorianCalendar(1995, 8, 20).getTime();

        User user = BuilderFactory.createUser()
                .id(5)
                .birthDate(birthDate)
                .build();

        // When
        repository.add(user);

        // Then
        verify(connectionSpy).prepareStatement(anyString());
        verify(connectionSpy).close();

        try (
                Connection connection = hsqldb.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM useraccount WHERE  id = 5")
        ) {
            while (resultSet.next()) {
                assertThat(resultSet.getString("firstname"), is("Test"));
                assertThat(resultSet.getString("lastname"), is("Person"));
                assertThat(resultSet.getString("email"), is("test@test.com"));
                assertThat(resultSet.getString("password"), is("123"));
                final long birthDate1 = resultSet.getDate("birthDate").getTime();
                assertThat(birthDate1, is(birthDate.getTime()));
            }
        }
    }

    @Test
    public void shouldAddAllUsers() throws Exception {
        // Given
        final MySqlUserRepository repository = new MySqlUserRepository(hsqldb::getConnection);
        final User user1 = BuilderFactory.createUser().id(1).build();
        final User user2 = BuilderFactory.createUser().id(2).build();
        final User user3 = BuilderFactory.createUser().id(3).build();

        // When
        repository.add(Arrays.asList(user1, user2, user3));

        final List<User> users = repository.query(new GetAllUsersSpecification());

        // Then
        assertThat(users, hasSize(3));
        assertThat(users, hasItems(user1, user2, user3));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnAlreadyExistingId() throws Exception {
        // Given
        final MySqlUserRepository repository = new MySqlUserRepository(() -> hsqldb.getConnection());
        final User user = BuilderFactory.createUser().build();

        // When
        repository.add(user);
        repository.add(user);

        // Then

    }

    @Test
    public void shouldQueryUsingSpecification() throws Exception {
        // Given
        SqlSpecification specification = new GetUserByIdSpecification(1);
        final MySqlUserRepository repository = new MySqlUserRepository(() -> hsqldb.getConnection());
        final User user = BuilderFactory.createUser().build();

        // When
        repository.add(user);
        final List<User> users = repository.query(specification);

        // Then
        assertThat(users, hasSize(1));
        assertThat(users, hasItem(user));
    }

    @Test
    public void shouldUpdateExisitingUser() throws Exception {
        // Given
        final MySqlUserRepository repository = new MySqlUserRepository(() -> hsqldb.getConnection());
        final User user = BuilderFactory.createUser().build();
        final User updatedUser = BuilderFactory.createUser().firstName("NOT SAME AS FIRST").build();

        // When
        repository.add(user);
        repository.update(updatedUser);

        final List<User> users = repository.query(new GetUserByIdSpecification(user.getId()));

        // Then
        final User result = users.get(0);
        assertThat(result.getId(), is(user.getId()));
        assertThat(result.getFirstName(), is(not(user.getFirstName())));
        assertThat(result.getFirstName(), is(updatedUser.getFirstName()));
    }

    @Test
    public void shouldRemoveUser() throws Exception {
        // Given
        final MySqlUserRepository repository = new MySqlUserRepository(() -> hsqldb.getConnection());
        final User user = BuilderFactory.createUser().build();

        // When
        repository.add(user);
        repository.remove(user);

        final List<User> users = repository.query(new GetUserByIdSpecification(user.getId()));

        // Then
        assertThat(users, is(empty()));
    }

    @Test
    public void shouldRemoveSpecifiedUsers() throws Exception {
        // Given
        final MySqlUserRepository repository = new MySqlUserRepository(hsqldb::getConnection);
        final User user1 = BuilderFactory.createUser().id(1).build();
        final User user2 = BuilderFactory.createUser().id(2).build();
        final User user3 = BuilderFactory.createUser().id(3).build();

        // When
        repository.add(Arrays.asList(user1, user2, user3));
        repository.remove(new GetAllUsersSpecification());

        repository.add(Arrays.asList(user1, user3));
        repository.remove(new GetUserByIdSpecification(1));

        final List<User> users = repository.query(new GetAllUsersSpecification());

        // Then
        assertThat(users, is(not(empty())));
        assertThat(users, hasItem(user3));
    }

    @Test
    public void shouldRemoveAllSpecifiedUsers() throws Exception {
        // Given
        final MySqlUserRepository repository = new MySqlUserRepository(() -> hsqldb.getConnection());
        final User user1 = BuilderFactory.createUser().id(1).build();
        final User user2 = BuilderFactory.createUser().id(2).build();
        final User user3 = BuilderFactory.createUser().id(3).build();

        // When
        repository.add(user1);
        repository.add(user2);
        repository.add(user3);

        repository.remove(Arrays.asList(user1, user3));

        final List<User> users = repository.query(new GetAllUsersSpecification());

        // Then
        assertThat(users, hasSize(1));
        assertThat(users, hasItem(user2));
    }

}