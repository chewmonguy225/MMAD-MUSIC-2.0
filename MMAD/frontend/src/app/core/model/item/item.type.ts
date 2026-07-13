export type MusicProvider =
    | 'SPOTIFY'
    | 'APPLE_MUSIC';

export interface Item {
    id: number | null;
    sourceId: string;
    provider: MusicProvider;
    name: string;
    imageURL: string;
    type: 'artist' | 'album' | 'song' | 'user';
}