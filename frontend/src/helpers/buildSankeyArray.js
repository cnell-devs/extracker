// reconstructs dataMap for insertion into Sankey component

import { washCategory } from "./washCategory";

export const buildSankeyArray = (dataMap) => {
  // 🔹 Step 1: Extract "INCOME" and remove from dataMap
  const income = dataMap.get("INCOME") || 1; // Default to 1 to prevent division by zero

  // 🔹 Step 2: Extract child node values (ignoring `INCOME`)
  const values = Array.from(dataMap.values());

  // 🔹 Step 3: Find min & max values for normalization (ignoring zero or negative values)
  const minWeight = Math.min(...values.filter((v) => v > 0));
  const maxWeight = Math.max(...values);

  // 🔹 Step 4: Normalize function (scale to 1-10)
  const normalize = (value) => {
    if (value <= 0) return 1; // Avoid zero or negative weights
    return 1 + 9 * ((value - minWeight) / (maxWeight - minWeight));
  };

  // 🔹 Step 5: Construct Sankey Data with Proper Weight & Percentage of Total Income
  const incomeLabel = `Income - $${income.toFixed()}`;

  const dataMap2 = new Map(dataMap);
  dataMap2.delete("INCOME");

  const sankeyData = [ 
    ["From", "To", "Weight"],
    ["Paychecks", incomeLabel, normalize(income)],
    [incomeLabel, "Expenses", normalize(income)],
  ].concat(
    Array.from(dataMap2).map(([key, value]) => {
      // 🔹 Convert keys into readable labels

      // 🔹 Step 6: Calculate percentage of total income
      // const percentageOfIncome = ((value / income) * 100).toFixed(1); // ✅ Percentage of total income
      const catLabel = `${washCategory(key)} - $${value.toFixed()}`; // ✅ Display category and percentage
      // const catLabel = `${key} - ${percentageOfIncome}%`; // ✅ Display category and percentage
      return ["Expenses", catLabel, normalize(value)]; // ✅ Normalize weights (1-10)
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
