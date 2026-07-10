package com.MMAD.Service.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MMAD.model.item.Album;
import com.MMAD.model.item.Artist;
import com.MMAD.model.item.Item;
import com.MMAD.model.item.Song;
import com.MMAD.repo.item.ItemRepo;

@Service
public class ItemResolver {

    private final ItemRepo itemRepo;

    public ItemResolver(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
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

        return (Artist) itemRepo
                .findByProviderAndSourceId(
                        artist.getProvider(),
                        artist.getSourceId())
                .orElseGet(() -> itemRepo.save(artist));
    }

    private Album resolveAlbum(Album album) {

        Optional<Item> existingAlbum = itemRepo.findByProviderAndSourceId(
                album.getProvider(),
                album.getSourceId());

        if (existingAlbum.isPresent()) {
            return (Album) existingAlbum.get();
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

        // Resolve artists
        if (song.getArtists() != null) {

            List<Artist> resolvedArtists = song.getArtists()
                    .stream()
                    .map(this::resolveArtist)
                    .collect(Collectors.toCollection(ArrayList::new));

            song.setArtists(resolvedArtists);
        }

        // Resolve album
        if (song.getAlbum() != null) {

            Album resolvedAlbum = resolveAlbum(song.getAlbum());

            song.setAlbum(resolvedAlbum);
        }

        return song;
    }
}