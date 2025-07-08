CREATE SCHEMA IF NOT EXISTS mmad;

SET NAMES 'UTF8MB4';
SET TIME_ZONE = '-4:00';

USE mmad;

DROP TABLE IF EXISTS user;

CREATE TABLE user (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS artist;

CREATE TABLE artist (
    id INT PRIMARY KEY AUTO_INCREMENT,
    source_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    imageurl VARCHAR(255) DEFAULT 'https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png'
);

DROP TABLE IF EXISTS album;

CREATE TABLE album (
    id INT PRIMARY KEY AUTO_INCREMENT,
    source_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    imageurl VARCHAR(255) DEFAULT 'https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png',
    artist_id INT NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artist(id)
);

DROP TABLE IF EXISTS song;

CREATE TABLE song (
    id INT PRIMARY KEY AUTO_INCREMENT,
    source_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    imageurl VARCHAR(255) DEFAULT 'https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png',
    album_id INT NOT NULL,
    artist_id INT NOT NULL,
    FOREIGN KEY (album_id) REFERENCES album(id),
    FOREIGN KEY (artist_id) REFERENCES artist(id)
);

DROP TABLE IF EXISTS playlist;

CREATE TABLE playlist (
    username VARCHAR(255) NOT NULL,
    song_id INT NOT NULL,
    PRIMARY KEY (username, song_id),
    FOREIGN KEY (username) REFERENCES user(username),
    FOREIGN KEY (song_id) REFERENCES song(id)
);

DROP TABLE IF EXISTS review;

CREATE TABLE review (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    rating INT NOT NULL,
    review_text VARCHAR(255) NOT NULL,
    song_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT rating_check CHECK (rating >= 0 AND rating <= 5),
    FOREIGN KEY (username) REFERENCES user(username),
    FOREIGN KEY (song_id) REFERENCES song(id)
);

DROP TABLE IF EXISTS user_friend;

CREATE TABLE user_friend (
    username VARCHAR(255) NOT NULL,
    friend_username VARCHAR(255) NOT NULL,
    PRIMARY KEY (username, friend_username),
    FOREIGN KEY (username) REFERENCES user(username),
    FOREIGN KEY (friend_username) REFERENCES user(username)
);
