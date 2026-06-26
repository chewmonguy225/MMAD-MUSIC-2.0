export abstract class Item {
    // No createdAt or updatedAt here for the item itself
    constructor(
        protected id: number,
        protected sourceId: string,
        protected name: string,
        protected imageURL: string,
        public type: 'ARTIST' | 'ALBUM' | 'SONG' // Crucial: Add the 'type' field directly here
    ) {}

    // Getters
    getId(): number {
        return this.id;
    }

    getSourceId(): string {
        return this.sourceId;
    }

    getName(): string {
        return this.name;
    }

    getImageURL(): string {
        // Provide a default image if imageURL is null, undefined, or empty string
        return this.imageURL || "https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png";
    }

    getType(): string{
        return this.type;
    }

    // Setters
    setId(id: number): void {
        this.id = id;
    }

    setSourceId(sourceId: string): void {
        this.sourceId = sourceId;
    }

    setName(name: string): void {
        this.name = name;
    }

    setImageURL(imageURL: string): void {
        this.imageURL = imageURL;
    }
}