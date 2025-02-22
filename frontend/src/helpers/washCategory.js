export const washCategory = (category) => {
  if (!category || typeof category !== "string") return "Unknown"; // ✅ Handle null/undefined cases

  return category
    .trim() // ✅ Remove unnecessary spaces
    .toLowerCase()
    .split(/[_\s]+/) // ✅ Split by underscores OR spaces (more flexible)
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
};
