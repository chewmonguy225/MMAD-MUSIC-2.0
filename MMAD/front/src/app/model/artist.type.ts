import { Item } from './item.type'

export class Artist extends Item{
    constructor(
        id: number = 0,
        sourceId: String = "",
        name: String,
        imageURL: String = "default.jpg") {
        super(id, sourceId, name, imageURL);
    }
}