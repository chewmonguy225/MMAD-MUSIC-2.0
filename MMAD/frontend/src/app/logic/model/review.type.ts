// src/app/model/review.type.ts

import { UserDTO } from "../service/user/user.service"; // Adjust path as needed
import { Album } from "./item/album.type";
import { Artist } from "./item/artist.type";
import { Item } from "./item/item.type"; // Adjust path as needed
import { Song } from "./item/song.type";

// --- Review Class with Explicit Getters and Setters (no '_' prefix) ---
export class Review {
  // Public properties (no '_' prefix for direct access within the class)
  public id: number;
  public rating: number;
  public description: string;
  public item: Item;
  public user: UserDTO;
  public createdAt?: string;
  public updatedAt?: string;

  constructor(
    id: number,
    rating: number,
    description: string,
    item: Item,
    user: UserDTO,
    createdAt?: string,
    updatedAt?: string
  ) {
    // Initialize public properties directly in the constructor
    this.id = id;
    this.rating = rating;
    this.description = description;
    this.item = item;
    this.user = user;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // --- Explicit Getters (method-based) ---
  public getId(): number {
    return this.id;
  }

  public getRating(): number {
    return this.rating;
  }

  public getDescription(): string {
    return this.description;
  }

  public getItem(): Item {
    return this.item;
  }

  public getUser(): UserDTO {
    return this.user;
  }

  public getCreatedAt(): string | undefined {
    return this.createdAt;
  }

  public getUpdatedAt(): string | undefined {
    return this.updatedAt;
  }

  // --- Explicit Setters (method-based) ---
  public setId(value: number): void {
    if (value <= 0) {
      console.warn("Review ID cannot be non-positive.");
      // Or throw new Error("Invalid ID");
    }
    this.id = value;
  }

  public setRating(value: number): void {
    if (value < 1 || value > 5) {
      console.warn("Rating must be between 1 and 5.");
      // Or throw new Error("Invalid Rating");
    }
    this.rating = value;
  }

  public setDescription(value: string): void {
    if (value.trim().length === 0) {
      console.warn("Description cannot be empty.");
    }
    this.description = value;
  }

  public setItem(value: Item): void {
    if (!value) {
      console.warn("Item cannot be null or undefined.");
    }
    this.item = value;
  }

  public setUser(value: UserDTO): void {
    if (!value) {
      console.warn("User cannot be null or undefined.");
    }
    this.user = value;
  }

  public setCreatedAt(value: string | undefined): void {
    // You could add date validation here
    this.createdAt = value;
  }

  public setUpdatedAt(value: string | undefined): void {
    // You could add date validation here
    this.updatedAt = value;
  }

  // Example of a computed getter (still works with explicit getters)
  // This property-like getter is still useful for derived values
  public get displayRating(): string {
    return `${this.rating} out of 5 stars`;
  }

  public static fromJSON(plainReview: any): Review {
    let itemInstance: Item;

    if (plainReview.item) {
      switch (plainReview.item.type) {
        case 'ARTIST':
          itemInstance = Artist.fromJson(plainReview.item);
          break;

        case 'ALBUM':
          itemInstance = Album.fromJson(plainReview.item);
          break;
        case 'SONG':
          itemInstance = Song.fromJson(plainReview.item);
          break;
        default:
          // Fallback for unknown 'itemType' or if 'itemType' is missing but plainReview.item exists
          console.warn(`Unknown item type encountered: ${plainReview.item.itemType}. Creating a generic/dummy Item.`);
          itemInstance = new Artist(-1, "-1", "-1", "-1");
          break;
      }
    } else {
      // Fallback if plainReview.item itself is null or undefined
      console.warn('Review item data is null or undefined. Creating a generic/dummy Item.');
      itemInstance = new Artist(-1, "-1", "-1", "-1");
    }

    // Return a new instance of your Review class
    return new Review(
      plainReview.id,
      plainReview.rating,
      plainReview.description,
      itemInstance, // Pass the instantiated Item
      plainReview.user, // UserDTO is likely just a plain object/interface, no need to instantiate unless it's a class
      plainReview.createdAt,
      plainReview.updatedAt
    );
  }
}

// --- Interfaces (remain as interfaces for DTO/payload structures) ---

// For POST /reviews (creation)
export interface ReviewPostRequestPayload {
  username: string;
  itemId: number;
  rating: number;
  description: string;
}

// For PUT /reviews/{id} (update)
export interface UpdateReviewPayload {
  rating: number;
  description: string;
}

// For Get /reviews (view)
export interface ReviewGetResponse {
  id: number;
  rating: number;
  description: string;
  itemId: number;
  // user?: UserDTO; // Uncomment if these are part of this response type
  // item?: Item;
  // createdAt?: string;
  // updatedAt?: string;
}