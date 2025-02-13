import { washCategory } from "../helpers/washCategory";

export const CatSelector = ({ categories, activeCat, setActiveCat }) => {
  categories = ["SHOW_ALL"].concat(Array.from(categories));
  // console.log(
  //   categories.map((category, i) => {
  //     return (
  //         washCategory(category)
  //     );
  //   })
  // );

  return (
    <div className="flex gap-2">
      {categories.map((category, i) => {
        return (
          <button
            className={`btn ${category == activeCat ? "bg-blue-300" : null}`}
            onClick={() => setActiveCat(category)}
            key={i}
          >
            {washCategory(category)}
          </button>
        );
      })}
    </div>
  );
};
