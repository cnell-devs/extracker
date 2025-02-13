import { Chart } from "react-google-charts";
import { buildSankeyArray } from "../helpers/buildSankeyArray";

export const Sankey = ({ dataMap, active}) => {

const {sankeyData, options} = buildSankeyArray(dataMap, active)

  return (<div className="w-3/4">

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
