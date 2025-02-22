import { washCategory } from "../helpers/washCategory";

// ✅ Extract categories dynamically
const extractCategories = (transactions) => {
  if (!transactions || transactions.length === 0) return [];

  const categories = transactions.map(
    ({ primaryCategory, amount }) => (amount < 0 ? "INCOME" : primaryCategory) // ✅ Classify negative amounts as INCOME
  );

  return ["SHOW_ALL", ...new Set(categories)]; // ✅ Ensure `SHOW_ALL` is first & remove duplicates
};

export const CatSelector = ({ data, activeCat, setActiveCat }) => {
  const categories = extractCategories(data);

  if (categories.length === 1) return <p>Loading categories...</p>; // ✅ Prevent empty UI state

  return (
    <div className="flex gap-2 flex-wrap">
          {categories.filter(c => c != "INCOME").map((category, i) => (

        <button
          className={`btn ${category === activeCat ? "bg-blue-300" : ""}`} // ✅ Fixed class condition
          onClick={() => setActiveCat(category)}
          key={i}
        >
          {washCategory(category)}
        </button>
      ))}
    </div>
  );
};
