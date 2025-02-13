import { washCategory } from "./washCategory";

export const buildSankeyArray = (dataMap, active) => {
  // 🔹 Step 1: Extract "INCOME" and remove from dataMap
  const income = dataMap.get("INCOME") || 1; // Default to 1 to prevent division by zero
  const dataMap2 = new Map(dataMap);
  dataMap2.delete("INCOME");

  // 🔹 Step 2: Extract child node values (ignoring `INCOME`)
  const values = Array.from(dataMap2.values());

  // 🔹 Step 3: Find min & max values for normalization (ignoring zero or negative values)
  const minWeight = Math.min(...values.filter((v) => v > 0));
  const maxWeight = Math.max(...values);

  // 🔹 Step 4: Normalize function (scale to 1-10, ensuring all values contribute)
  const normalize = (value) => {
    if (value <= 0 || maxWeight === minWeight) return 1; // Avoid zero or negative weights & division by zero
    return 1 + 9 * ((value - minWeight) / (maxWeight - minWeight));
  };

  // 🔹 Step 5: Ensure Parent Category = Sum of Child Normalized Weights
  const categorySum = () => {
    const values = Array.from(dataMap2.values());
    return values.length
      ? values.reduce((acc, curr) => acc + normalize(curr), 0)
      : 1; // ✅ Sum of normalized values
  };
  console.log(dataMap2);

  const catWeightSum = categorySum(); // Get total child category weight
  const incomeLabel = `Income - $${income.toFixed()}`;
  const parentLabel = `${washCategory(active)} - $${dataMap2
    .values()
    .reduce((acc, curr) => acc + curr).toFixed(2)}`;
  console.log(active);

  console.log(dataMap2);

  const sankeyData = [
    ["From", "To", "Weight"],
    active === "SHOW_ALL"
      ? [incomeLabel, "Expenses", normalize(income)]
      : [
          parentLabel,
          "Expenses",
          catWeightSum, // ✅ Ensure Parent == Sum of Children (normalized)
        ],
  ].concat(
    Array.from(dataMap2).map(([key, value]) => {
      const catLabel = `${washCategory(
        key.replace(active, "")
      )} - $${value.toFixed(2)}`;
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
