import { Item } from './item.type'

export class Artist extends Item{
    constructor(
        id: number = 0,
        sourceID: String = "",
        name: String,
        imageURL: String = "default.jpg") {
        super(id, sourceID, name, imageURL);
    }
}