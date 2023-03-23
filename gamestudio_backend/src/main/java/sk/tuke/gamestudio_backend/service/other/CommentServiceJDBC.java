package sk.tuke.gamestudio_backend.service.other;

import sk.tuke.gamestudio_backend.service.interfaces.CommentService;
import sk.tuke.gamestudio_backend.databaseJDBC.DBConnection;
import sk.tuke.gamestudio_backend.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {

    private static final String INSERT = "INSERT INTO comments (player, game, comment, commentedOn) VALUES (?, ?, ?, ?)";
    private static final String SELECT = "SELECT player, game, comment, commentedOn FROM comments WHERE game = ? ORDER BY commentedOn DESC";
    private static final String RESET = "TRUNCATE TABLE comments RESTART IDENTITY";
    private static final String CREATE = """
                CREATE TABLE IF NOT EXISTS comments (
                id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                player VARCHAR(50) NOT NULL,
                game VARCHAR(50) NOT NULL,
                comment VARCHAR(300) NOT NULL,
                commentedOn TIMESTAMP
            );
            """;

    public void createCommentTable() {
        try(Connection connection = DBConnection.getConnection();
        Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE);

        } catch (SQLException e) { throw new CommentException("cant create comments table"); }
    }

    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {

            preparedStatement.setString(1, comment.getGame());
            preparedStatement.setString(2, comment.getPlayer());
            preparedStatement.setString(3, comment.getComment());
            preparedStatement.setTimestamp(4, comment.getCommentedOn());

            preparedStatement.executeUpdate();

        } catch (SQLException e) { throw new CommentException("cant add comment"); }
    }

    @Override
    public  List<Comment> getComments(String game) {
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {

            preparedStatement.setString(1,game);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    Comment newComment = new Comment(rs.getString("player"), rs.getString("game"), rs.getString("comment"), rs.getTimestamp("commentedOn"));
                    comments.add(newComment);
                }
                return comments;
            }

        } catch (SQLException e) { throw new CommentException("cant get comments"); }
    }

    @Override
    public void reset() throws CommentException {
        try(Connection connection = DBConnection.getConnection();
        Statement statement = connection.createStatement()) {

            statement.executeUpdate(RESET);

        } catch (SQLException e) { throw new CommentException("cant reset comments"); }
    }

    @Override
    public void deleteCommentById(Long id) {

    }
}
