import { Chart } from "react-google-charts";
import { buildSankeyArray } from "../helpers/buildSankeyArray";

const generateDataMap = (transactions, activeCat) => {
  if (!transactions || transactions.length === 0) return new Map();


  const newDataMap = new Map();

  // ✅ Identify total income (only transactions where primaryCategory === "INCOME")
  const totalIncome = transactions
    .filter(({ primaryCategory }) => primaryCategory === "INCOME")
    .reduce((acc, { amount }) => acc + Math.abs(amount), 0);

  console.log("totalIncome", totalIncome);

  transactions
    .filter(({ primaryCategory }) =>
      activeCat === "SHOW_ALL" ? true : primaryCategory === activeCat
    )
    .forEach(({ primaryCategory, detailedCategory, amount }) => {
      const transactionAmount = Number(amount) || 0;

      if (primaryCategory === "INCOME") return; // ✅ Skip income transactions when building expenses

      if (activeCat === "SHOW_ALL") {
        // ✅ Store `primaryCategory`
        const catTotal = newDataMap.get(primaryCategory) || 0;
        newDataMap.set(primaryCategory, catTotal + transactionAmount);
      } else {
        // ✅ Store `detailedCategory` under the selected `primaryCategory`
        const catTotal = newDataMap.get(detailedCategory) || 0;
        newDataMap.set(
          detailedCategory,
          catTotal + transactionAmount
        );
      }
    });

  // ✅ Ensure INCOME is always included
  newDataMap.set("INCOME", totalIncome);

  console.log(newDataMap);
  return newDataMap;
};

export const Sankey = ({ data, active }) => {
  if (!data || data.length === 0) {
    return <p className="text-gray-500">No data available</p>; // ✅ Prevents rendering errors
  }

  const dataMap = generateDataMap(data, active); // ✅ Ensures correct data categorization
  const { sankeyData, options } = buildSankeyArray(dataMap, active);

  return (
    <div className="w-3/4">
      <Chart
        chartVersion="51"
        chartType="Sankey"
        width="100%"
        height="70vh"
        data={sankeyData}
        options={options}
        className="flex justify-center"
      />
    </div>
  );
};
