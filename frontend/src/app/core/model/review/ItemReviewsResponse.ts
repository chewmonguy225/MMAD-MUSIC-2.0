export interface ItemReviewsResponse {
  itemId: number;
  reviews: ItemReviewViewModel[];
}

export interface ItemReviewViewModel {
  id: number;
  rating: number;
  description: string;
  username: string;
  createdAt: string;
  updatedAt: string;
}
