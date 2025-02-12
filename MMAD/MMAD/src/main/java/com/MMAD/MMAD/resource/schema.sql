CREATE SCHEMA IF NOT EXISTS MMAD;

SET NAMES 'UTF8MB4';
SET TIME_ZONE = '-4:00';

USE MMAD;

DROP TABLE IF EXISTS User;

CREATE TABLE User (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
);

DROP TABLE IF EXISTS Artist;

CREATE TABLE Artist (
    id INT PRIMARY KEY AUTO_INCREMENT,
    source_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    imageurl VARCHAR(255) DEFAULT 'https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png'
);

DROP TABLE IF EXISTS Album;

CREATE TABLE Album (
    id INT PRIMARY KEY AUTO_INCREMENT,
    source_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    imageurl VARCHAR(255) DEFAULT 'https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png',
    artist_id INT NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES Artist(id)
);

DROP TABLE IF EXISTS Song;

CREATE TABLE Song (
    id INT PRIMARY KEY AUTO_INCREMENT,
    source_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    imageurl VARCHAR(255) DEFAULT 'https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png',
    album_id INT NOT NULL,
    FOREIGN KEY (album_id) REFERENCES Album(id)
    artist_id INT NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES Artist(id)
);

DROP TABLE IF EXISTS Playlist;

CREATE TABLE Playlist (
    username VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES User(username)
    song_id INT NOT NULL,
    FOREIGN KEY (song_id) REFERENCES Song(id),
    PRIMARY KEY (username, song_id)
);

DROP TABLE IF EXISTS Review;

CREATE TABLE Review (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES User(username)
    rating INT NOT NULL,
    text VARCHAR(255) NOT NULL,
    song_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT rating CHECK (rating >= 0 AND rating <= 5)
);

DROP TABLE IF EXISTS user_friends;

CREATE TABLE user_friends (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (friend_id) REFERENCES User(id)
);