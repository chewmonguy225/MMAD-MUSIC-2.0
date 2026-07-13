package com.MMAD.Service.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MMAD.Service.SpotifyService;
import com.MMAD.entity.item.Album;
import com.MMAD.entity.item.Artist;
import com.MMAD.entity.item.Item;
import com.MMAD.entity.item.MusicProvider;
import com.MMAD.entity.item.Song;
import com.MMAD.repo.item.ItemRepo;

@Service
public class ItemResolver {

    private final ItemRepo itemRepo;
    private final SpotifyService spotifyService;

    public ItemResolver(
            ItemRepo itemRepo,
            SpotifyService spotifyService) {

        this.itemRepo = itemRepo;
        this.spotifyService = spotifyService;
    }

    @Transactional
    public Item resolve(Item item) {

        if (item instanceof Artist artist) {
            return resolveArtist(artist);
        }

        if (item instanceof Album album) {
            return resolveAlbum(album);
        }

        if (item instanceof Song song) {
            return resolveSong(song);
        }

        return item;
    }

    private Artist resolveArtist(Artist artist) {

        Optional<Item> existing = itemRepo.findByProviderAndSourceId(
                artist.getProvider(),
                artist.getSourceId());

        if (existing.isPresent()
                && existing.get() instanceof Artist existingArtist) {

            return existingArtist;
        }

        return itemRepo.save(artist);
    }

    private Album resolveAlbum(Album album) {

        Optional<Item> existing = itemRepo.findByProviderAndSourceId(
                album.getProvider(),
                album.getSourceId());

        if (existing.isPresent()
                && existing.get() instanceof Album existingAlbum) {

            return existingAlbum;
        }

        if (album.getArtists() != null) {

            List<Artist> resolvedArtists = album.getArtists()
                    .stream()
                    .map(this::resolveArtist)
                    .collect(Collectors.toCollection(ArrayList::new));

            album.setArtists(resolvedArtists);
        }

        return itemRepo.save(album);
    }

    private Song resolveSong(Song song) {

        Optional<Item> existing = itemRepo.findByProviderAndSourceId(
                song.getProvider(),
                song.getSourceId());

        if (existing.isPresent()
                && existing.get() instanceof Song existingSong) {

            return existingSong;
        }

        if (song.getArtists() != null) {

            List<Artist> resolvedArtists = song.getArtists()
                    .stream()
                    .map(this::resolveArtist)
                    .collect(Collectors.toCollection(ArrayList::new));

            song.setArtists(resolvedArtists);
        }

        if (song.getAlbum() != null) {

            Album resolvedAlbum = resolveAlbum(song.getAlbum());

            song.setAlbum(resolvedAlbum);
        }

        return itemRepo.save(song);
    }

    public Item resolveSpotifyItem(
            String type,
            String sourceId,
            MusicProvider provider) {

        if (provider != MusicProvider.SPOTIFY) {

            throw new RuntimeException(
                    "Unsupported provider");
        }

        return switch (type) {

            case "artist" ->
                spotifyService
                        .getArtist(sourceId)
                        .toEntity();

            case "album" ->
                spotifyService
                        .getAlbum(sourceId)
                        .toEntity();

            case "song" ->
                spotifyService
                        .getSong(sourceId)
                        .toEntity();

            default ->
                throw new RuntimeException(
                        "Unknown item type");
        };
    }

    @Transactional
    public Item refresh(Item item) {

        if (item instanceof Artist artist) {

            refreshArtist(artist);

            return itemRepo.save(artist);
        }

        if (item instanceof Album album) {

            refreshAlbum(album);

            if (album.getArtists() != null) {

                album.getArtists()
                        .forEach(this::refreshArtist);
            }

            return itemRepo.save(album);
        }

        if (item instanceof Song song) {

            if (song.getAlbum() != null) {

                refreshAlbum(song.getAlbum());
            }

            if (song.getArtists() != null) {

                song.getArtists()
                        .forEach(this::refreshArtist);
            }

            return itemRepo.save(song);
        }

        return item;
    }

    private void refreshArtist(Artist artist) {

        if (artist.getProvider() == MusicProvider.SPOTIFY
                && (artist.getImageURL() == null
                        || artist.getImageURL().contains("default"))) {

            Artist spotifyArtist = spotifyService
                    .getArtist(artist.getSourceId())
                    .toEntity();

            artist.setImageURL(
                    spotifyArtist.getImageURL());

            itemRepo.save(artist);
        }
    }

    private void refreshAlbum(Album album) {

        if (album.getProvider() == MusicProvider.SPOTIFY
                && (album.getImageURL() == null
                        || album.getImageURL().contains("default"))) {

            Album spotifyAlbum = spotifyService
                    .getAlbum(album.getSourceId())
                    .toEntity();

            album.setImageURL(
                    spotifyAlbum.getImageURL());

            itemRepo.save(album);
        }
    }

}