import { UserRole } from '../types/types';
export function roleToPascalCase(role: UserRole) {
  return role.charAt(0).toUpperCase() + role.slice(1).toLowerCase();
}
