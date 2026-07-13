export function isValidEmail(email: string): boolean {

  const emailPattern =
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  return emailPattern.test(email);

}


export interface PasswordValidation {

  hasMinLength: boolean;

  hasUppercase: boolean;

  hasLowercase: boolean;

  hasNumber: boolean;

  isValid: boolean;

}


export function validatePassword(password: string): PasswordValidation {

  const result = {

    hasMinLength: password.length >= 8,

    hasUppercase: /[A-Z]/.test(password),

    hasLowercase: /[a-z]/.test(password),

    hasNumber: /\d/.test(password)

  };


  return {

    ...result,

    isValid:
      result.hasMinLength &&
      result.hasUppercase &&
      result.hasLowercase &&
      result.hasNumber

  };

}


export function isValidPassword(password: string): boolean {

  return validatePassword(password).isValid;

} 