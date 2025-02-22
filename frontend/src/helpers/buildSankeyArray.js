import { washCategory } from "../helpers/washCategory";

export const buildSankeyArray = (dataMap, active) => {
  if (!dataMap || dataMap.size === 0) {
    return { sankeyData: [["From", "To", "Weight"]], options: {} };
  }

  // 🔹 Step 1: Extract "INCOME" and remove from dataMap
  const income = dataMap.get("INCOME"); // ✅ Default to 1 to prevent division by zero
  const dataMap2 = new Map(dataMap);
  dataMap2.delete("INCOME");

  // 🔹 Step 2: Extract child node values (ignoring `INCOME`)
  const values = Array.from(dataMap2.values());

  // 🔹 Step 3: Find min & max values for normalization (ignoring zero or negative values)
  const validValues = values.filter((v) => v > 0);
  const minWeight = validValues.length ? Math.min(...validValues) : 1;
  const maxWeight = validValues.length ? Math.max(...validValues) : 1;

  // 🔹 Step 4: Normalize function (scale to 1-10, ensuring all values contribute)
  const normalize = (value) => {
    if (value <= 0 || maxWeight === minWeight) return 1; // ✅ Prevent zero weights & division by zero
    return 1 + 9 * ((value - minWeight) / (maxWeight - minWeight));
  };

  // 🔹 Step 5: Ensure Parent Category = Sum of Child Normalized Weights
  const categorySum = () => {
    return validValues.length
      ? validValues.reduce((acc, curr) => acc + normalize(curr), 0)
      : 1; // ✅ Sum of normalized values or default to 1
  };

  const catWeightSum = categorySum();
  const incomeLabel = `Income ($) : ${income.toFixed(2)}`;

  // ✅ Prevent `reduce()` error if `dataMap2` is empty
  const totalExpenses = dataMap2.size
    ? Array.from(dataMap2.values())
        .reduce((acc, curr) => acc + curr, 0)
        .toFixed(2)
    : 0;

  const parentLabel = `${washCategory(active)} Expenses ($) : ${totalExpenses}`;

  const sankeyData = [
    ["From", "To", "Weight"],
    active === "SHOW_ALL"
      ? [incomeLabel, "Expenses", normalize(income)]
      : [parentLabel, "Expenses", catWeightSum], // ✅ Ensure Parent == Sum of Children (normalized)
  ].concat(
    Array.from(dataMap2).map(([key, value]) => {
      const catLabel = `${washCategory(key)} ($) : ${value.toFixed(2)}`;
      return ["Expenses", catLabel, normalize(value)];
    })
  );

  const options = {
    sankey: {
      node: {
        label: { fontSize: 14 },
        nodePadding: 10, // ✅ Increases vertical space between nodes
        interactivity: true,
      },
      link: {
        colorMode: "gradient",
      },
    },
  };

  return { sankeyData, options };
};
