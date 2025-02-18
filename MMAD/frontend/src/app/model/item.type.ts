export abstract class Item{
    constructor(
        protected id: number = 0,
        protected sourceId: String = "",
        protected name: String,
        protected imageURL: String = "default.jpg") { }

    getImageURL(): string {
        return `${this.imageURL}`;
    }

    getId(): number {
        return this.id;
    }

    getSourceId(): string {
        return `${this.sourceId}`;
    }

    getName(): string {
        return `${this.name}`;
    }
}