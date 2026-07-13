export interface User {
  username: string;
  profilePicUrl: string;
  followers?: User[]; 
  following?: User[];  
}
