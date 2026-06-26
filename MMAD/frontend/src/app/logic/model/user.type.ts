export interface User {
  username: string;
  followers?: User[]; 
  following?: User[];  
}
