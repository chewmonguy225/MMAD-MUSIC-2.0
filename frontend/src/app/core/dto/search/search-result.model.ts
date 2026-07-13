export interface SearchResult {
  id: number;
  name: string;
  imageURL: string;
  sourceId: string;
  provider: 'SPOTIFY';

  type: 'artist' | 'album' | 'song' | 'user';

  artists?: string[];
}