package com.bookrating.dao;

import com.bookrating.model.Rating;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {
    
    // 添加评分
    public boolean addRating(Rating rating) {
        String sql = "INSERT INTO ratings (user_id, book_id, rating, comment) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rating.getUserId());
            pstmt.setInt(2, rating.getBookId());
            pstmt.setDouble(3, rating.getRating());
            pstmt.setString(4, rating.getComment());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 获取图书的所有评分
    public List<Rating> getRatingsByBookId(int bookId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings WHERE book_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("id"));
                rating.setUserId(rs.getInt("user_id"));
                rating.setBookId(rs.getInt("book_id"));
                rating.setRating(rs.getDouble("rating"));
                rating.setComment(rs.getString("comment"));
                rating.setCreatedAt(rs.getTimestamp("created_at"));
                ratings.add(rating);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }
    
    // 获取用户的所有评分
    public List<Rating> getRatingsByUserId(int userId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("id"));
                rating.setUserId(rs.getInt("user_id"));
                rating.setBookId(rs.getInt("book_id"));
                rating.setRating(rs.getDouble("rating"));
                rating.setComment(rs.getString("comment"));
                rating.setCreatedAt(rs.getTimestamp("created_at"));
                ratings.add(rating);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }
    
    // 检查用户是否已经对某本书评分
    public boolean hasUserRatedBook(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM ratings WHERE user_id = ? AND book_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 更新评分
    public boolean updateRating(Rating rating) {
        String sql = "UPDATE ratings SET rating = ?, comment = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, rating.getRating());
            pstmt.setString(2, rating.getComment());
            pstmt.setInt(3, rating.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 删除评分
    public boolean deleteRating(int ratingId) {
        String sql = "DELETE FROM ratings WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ratingId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 获取图书的平均评分
    public double getAverageRatingByBookId(int bookId) {
        String sql = "SELECT AVG(rating) FROM ratings WHERE book_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}