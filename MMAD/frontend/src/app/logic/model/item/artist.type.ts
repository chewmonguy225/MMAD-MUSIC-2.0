import { Item } from "./item.type";

export class Artist extends Item {
  constructor(
    id: number,
    sourceId: string,
    name: string,
    imageURL: string

  ) {

    super(id, sourceId, name, imageURL, 'ARTIST');
  }

  static fromJson(json: any): Artist {
    return new Artist(
      json.id,
      json.sourceId,
      json.name,
      json.imageURL
    );
  }
}