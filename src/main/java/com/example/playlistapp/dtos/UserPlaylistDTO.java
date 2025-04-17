package com.example.playlistapp.dtos;

public class UserPlaylistDTO{
    private String spotifyPlaylistId;
    private Long id;
    private String name;

    public UserPlaylistDTO(String spotifyPlaylistId, Long id, String name) {
        this.spotifyPlaylistId = spotifyPlaylistId;
        this.id = id;
        this.name = name;
    }

    public String getSpotifyPlaylistId() {
        return spotifyPlaylistId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}