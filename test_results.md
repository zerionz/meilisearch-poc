# Test Results

```
+--------+---------------+-------------------------------+----------------------------------------------------+----------------------------------------------------------------------------------+
| STATUS | TEST          | INPUT                         | EXPECT                                             | OUTPUT                                                                           |
+--------+---------------+-------------------------------+----------------------------------------------------+----------------------------------------------------------------------------------+
| PASS   | auto-1-term   | การส่งเสริมการลงทุน           | should include 'การส่งเสริมการลงทุน' (direct term) | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-1-syn-1  | การสนับสนุนการลงทุน           | should include 'การส่งเสริมการลงทุน' via synonym   | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-1-syn-2  | การยื่นขอรับการส่งเสริม       | should include 'การส่งเสริมการลงทุน' via synonym   | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-1-syn-3  | มาตรการส่งเสริมการลงทุน       | should include 'การส่งเสริมการลงทุน' via synonym   | hits=2 titles=การส่งเสริมการลงทุน, สิทธิประโยชน์                                 |
| PASS   | auto-2-term   | สิทธิประโยชน์                 | should include 'สิทธิประโยชน์' (direct term)       | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | auto-2-syn-1  | สิทธิและประโยชน์              | should include 'สิทธิประโยชน์' via synonym         | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | auto-2-syn-2  | ผลประโยชน์ทางการลงทุน         | should include 'สิทธิประโยชน์' via synonym         | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | auto-2-syn-3  | มาตรการส่งเสริม               | should include 'สิทธิประโยชน์' via synonym         | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | auto-3-term   | ยกเว้นภาษีเงินได้นิติบุคคล    | should include 'ยกเว้นภาษีเงินได้นิติบุคคล' (di... | hits=1 titles=ยกเว้นภาษีเงินได้นิติบุคคล                                         |
| FAIL   | auto-3-syn-1  | การงดเว้นภาษีกำไรบริษัท       | should include 'ยกเว้นภาษีเงินได้นิติบุคคล' via... | hits=0 titles=<none>                                                             |
| PASS   | auto-3-syn-2  | สิทธิประโยชน์ด้านภาษี         | should include 'ยกเว้นภาษีเงินได้นิติบุคคล' via... | hits=2 titles=ยกเว้นภาษีเงินได้นิติบุคคล, สิทธิประโยชน์                          |
| PASS   | auto-3-syn-3  | Tax Exemption                 | should include 'ยกเว้นภาษีเงินได้นิติบุคคล' via... | hits=1 titles=ยกเว้นภาษีเงินได้นิติบุคคล                                         |
| PASS   | auto-4-term   | การรายงานผลการดำเนินงาน       | should include 'การรายงานผลการดำเนินงาน' (direc... | hits=1 titles=การรายงานผลการดำเนินงาน                                            |
| PASS   | auto-4-syn-1  | e-Monitoring                  | should include 'การรายงานผลการดำเนินงาน' via sy... | hits=1 titles=การรายงานผลการดำเนินงาน                                            |
| PASS   | auto-4-syn-2  | การรายงานความคืบหน้าโครงการ   | should include 'การรายงานผลการดำเนินงาน' via sy... | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-4-syn-3  | รายงานประจำปี                 | should include 'การรายงานผลการดำเนินงาน' via sy... | hits=1 titles=การรายงานผลการดำเนินงาน                                            |
| PASS   | auto-5-term   | การโอนกิจการ                  | should include 'การโอนกิจการ' (direct term)        | hits=4 titles=การโอนกิจการ, การส่งเสริมการลงทุน, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-5-syn-1  | การควบรวมกิจการ               | should include 'การโอนกิจการ' via synonym          | hits=1 titles=การโอนกิจการ                                                       |
| PASS   | auto-5-syn-2  | การรับโอนโครงการ              | should include 'การโอนกิจการ' via synonym          | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-5-syn-3  | การรับโอนสิทธิ                | should include 'การโอนกิจการ' via synonym          | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-6-term   | ช่างฝีมือและผู้ชำนาญการ       | should include 'ช่างฝีมือและผู้ชำนาญการ' (direc... | hits=1 titles=ช่างฝีมือและผู้ชำนาญการ                                            |
| PASS   | auto-6-syn-1  | ผู้เชี่ยวชาญต่างชาติ          | should include 'ช่างฝีมือและผู้ชำนาญการ' via sy... | hits=1 titles=ช่างฝีมือและผู้ชำนาญการ                                            |
| PASS   | auto-6-syn-2  | บุคลากรทักษะสูง               | should include 'ช่างฝีมือและผู้ชำนาญการ' via sy... | hits=1 titles=ช่างฝีมือและผู้ชำนาญการ                                            |
| FAIL   | auto-6-syn-3  | Strategic Talent Center (STC) | should include 'ช่างฝีมือและผู้ชำนาญการ' via sy... | hits=0 titles=<none>                                                             |
| PASS   | auto-7-term   | ส่วนสูญเสีย                   | should include 'ส่วนสูญเสีย' (direct term)         | hits=1 titles=ส่วนสูญเสีย                                                        |
| FAIL   | auto-7-syn-1  | วัตถุดิบที่เสียหายจากการผลิต  | should include 'ส่วนสูญเสีย' via synonym           | hits=0 titles=<none>                                                             |
| FAIL   | auto-7-syn-2  | วัตถุดิบไม่ได้คุณภาพ          | should include 'ส่วนสูญเสีย' via synonym           | hits=0 titles=<none>                                                             |
| PASS   | auto-7-syn-3  | เศษซาก                        | should include 'ส่วนสูญเสีย' via synonym           | hits=1 titles=ส่วนสูญเสีย                                                        |
| PASS   | auto-8-term   | บัตรส่งเสริม                  | should include 'บัตรส่งเสริม' (direct term)        | hits=1 titles=บัตรส่งเสริม                                                       |
| PASS   | auto-8-syn-1  | ใบรับรองการส่งเสริม           | should include 'บัตรส่งเสริม' via synonym          | hits=1 titles=บัตรส่งเสริม                                                       |
| PASS   | auto-8-syn-2  | เอกสารยืนยันการได้รับสิทธิ    | should include 'บัตรส่งเสริม' via synonym          | hits=12 titles=การส่งเสริมการลงทุน, สิทธิประโยชน์, ยกเว้นภาษีเงินได้นิติบุคคล... |
| PASS   | auto-9-term   | การเปิดดำเนินการ              | should include 'การเปิดดำเนินการ' (direct term)    | hits=4 titles=การเปิดดำเนินการ, การส่งเสริมการลงทุน, การโอนกิจการ, การรายงานผ... |
| PASS   | auto-9-syn-1  | การเริ่มดำเนินกิจการ          | should include 'การเปิดดำเนินการ' via synonym      | hits=4 titles=การเปิดดำเนินการ, การส่งเสริมการลงทุน, การโอนกิจการ, การรายงานผ... |
| PASS   | auto-9-syn-2  | การตรวจสอบเงื่อนไขการผลิต     | should include 'การเปิดดำเนินการ' via synonym      | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | auto-10-term  | อุตสาหกรรมเป้าหมาย            | should include 'อุตสาหกรรมเป้าหมาย' (direct term)  | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | auto-10-syn-1 | อุตสาหกรรมยุทธศาสตร์          | should include 'อุตสาหกรรมเป้าหมาย' via synonym    | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | auto-10-syn-2 | S-Curve                       | should include 'อุตสาหกรรมเป้าหมาย' via synonym    | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | auto-10-syn-3 | Targeted Industries           | should include 'อุตสาหกรรมเป้าหมาย' via synonym    | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | auto-11-term  | สูตรการผลิต                   | should include 'สูตรการผลิต' (direct term)         | hits=1 titles=สูตรการผลิต                                                        |
| PASS   | auto-11-syn-1 | บัญชีปริมาณสต๊อก              | should include 'สูตรการผลิต' via synonym           | hits=1 titles=สูตรการผลิต                                                        |
| FAIL   | auto-11-syn-2 | อัตราการใช้ส่วนผสมวัตถุดิบ    | should include 'สูตรการผลิต' via synonym           | hits=0 titles=<none>                                                             |
| PASS   | auto-12-term  | ผู้พำนักระยะยาว               | should include 'ผู้พำนักระยะยาว' (direct term)     | hits=1 titles=ผู้พำนักระยะยาว                                                    |
| PASS   | auto-12-syn-1 | LTR Visa                      | should include 'ผู้พำนักระยะยาว' via synonym       | hits=1 titles=ผู้พำนักระยะยาว                                                    |
| PASS   | auto-12-syn-2 | Long-Term Resident            | should include 'ผู้พำนักระยะยาว' via synonym       | hits=1 titles=ผู้พำนักระยะยาว                                                    |
| PASS   | positive-01   | การสนับสนุนการลงทุน           | should include 'การส่งเสริมการลงทุน'               | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-02   | สิทธิและประโยชน์              | should include 'สิทธิประโยชน์'                     | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | positive-03   | Tax Exemption                 | should include 'ยกเว้นภาษีเงินได้นิติบุคคล'        | hits=1 titles=ยกเว้นภาษีเงินได้นิติบุคคล                                         |
| PASS   | positive-04   | e-Monitoring                  | should include 'การรายงานผลการดำเนินงาน'           | hits=1 titles=การรายงานผลการดำเนินงาน                                            |
| PASS   | positive-05   | การควบรวมกิจการ               | should include 'การโอนกิจการ'                      | hits=1 titles=การโอนกิจการ                                                       |
| FAIL   | positive-06   | Strategic Talent Center (STC) | should include 'ช่างฝีมือและผู้ชำนาญการ'           | hits=0 titles=<none>                                                             |
| PASS   | positive-07   | เศษซาก                        | should include 'ส่วนสูญเสีย'                       | hits=1 titles=ส่วนสูญเสีย                                                        |
| PASS   | positive-08   | ใบรับรองการส่งเสริม           | should include 'บัตรส่งเสริม'                      | hits=1 titles=บัตรส่งเสริม                                                       |
| PASS   | positive-09   | การเริ่มดำเนินกิจการ          | should include 'การเปิดดำเนินการ'                  | hits=4 titles=การเปิดดำเนินการ, การส่งเสริมการลงทุน, การโอนกิจการ, การรายงานผ... |
| PASS   | positive-10   | Targeted Industries           | should include 'อุตสาหกรรมเป้าหมาย'                | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | positive-11   | บัญชีปริมาณสต๊อก              | should include 'สูตรการผลิต'                       | hits=1 titles=สูตรการผลิต                                                        |
| PASS   | positive-12   | LTR Visa                      | should include 'ผู้พำนักระยะยาว'                   | hits=1 titles=ผู้พำนักระยะยาว                                                    |
| PASS   | positive-13   | การส่งเสริมการลงทุน           | should include 'การส่งเสริมการลงทุน'               | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-14   | การยื่นขอรับการส่งเสริม       | should include 'การส่งเสริมการลงทุน'               | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-15   | มาตรการส่งเสริมการลงทุน       | should include 'การส่งเสริมการลงทุน'               | hits=2 titles=การส่งเสริมการลงทุน, สิทธิประโยชน์                                 |
| PASS   | positive-16   | สิทธิประโยชน์                 | should include 'สิทธิประโยชน์'                     | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | positive-17   | ผลประโยชน์ทางการลงทุน         | should include 'สิทธิประโยชน์'                     | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | positive-18   | มาตรการส่งเสริม               | should include 'สิทธิประโยชน์'                     | hits=1 titles=สิทธิประโยชน์                                                      |
| PASS   | positive-19   | ยกเว้นภาษีเงินได้นิติบุคคล    | should include 'ยกเว้นภาษีเงินได้นิติบุคคล'        | hits=1 titles=ยกเว้นภาษีเงินได้นิติบุคคล                                         |
| FAIL   | positive-20   | การงดเว้นภาษีกำไรบริษัท       | should include 'ยกเว้นภาษีเงินได้นิติบุคคล'        | hits=0 titles=<none>                                                             |
| PASS   | positive-21   | สิทธิประโยชน์ด้านภาษี         | should include 'ยกเว้นภาษีเงินได้นิติบุคคล'        | hits=2 titles=ยกเว้นภาษีเงินได้นิติบุคคล, สิทธิประโยชน์                          |
| PASS   | positive-22   | การรายงานผลการดำเนินงาน       | should include 'การรายงานผลการดำเนินงาน'           | hits=1 titles=การรายงานผลการดำเนินงาน                                            |
| PASS   | positive-23   | การรายงานความคืบหน้าโครงการ   | should include 'การรายงานผลการดำเนินงาน'           | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-24   | รายงานประจำปี                 | should include 'การรายงานผลการดำเนินงาน'           | hits=1 titles=การรายงานผลการดำเนินงาน                                            |
| PASS   | positive-25   | การโอนกิจการ                  | should include 'การโอนกิจการ'                      | hits=4 titles=การโอนกิจการ, การส่งเสริมการลงทุน, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-26   | การรับโอนโครงการ              | should include 'การโอนกิจการ'                      | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-27   | การรับโอนสิทธิ                | should include 'การโอนกิจการ'                      | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-28   | ช่างฝีมือและผู้ชำนาญการ       | should include 'ช่างฝีมือและผู้ชำนาญการ'           | hits=1 titles=ช่างฝีมือและผู้ชำนาญการ                                            |
| PASS   | positive-29   | ผู้เชี่ยวชาญต่างชาติ          | should include 'ช่างฝีมือและผู้ชำนาญการ'           | hits=1 titles=ช่างฝีมือและผู้ชำนาญการ                                            |
| PASS   | positive-30   | บุคลากรทักษะสูง               | should include 'ช่างฝีมือและผู้ชำนาญการ'           | hits=1 titles=ช่างฝีมือและผู้ชำนาญการ                                            |
| PASS   | positive-31   | ส่วนสูญเสีย                   | should include 'ส่วนสูญเสีย'                       | hits=1 titles=ส่วนสูญเสีย                                                        |
| FAIL   | positive-32   | วัตถุดิบที่เสียหายจากการผลิต  | should include 'ส่วนสูญเสีย'                       | hits=0 titles=<none>                                                             |
| FAIL   | positive-33   | วัตถุดิบไม่ได้คุณภาพ          | should include 'ส่วนสูญเสีย'                       | hits=0 titles=<none>                                                             |
| PASS   | positive-34   | บัตรส่งเสริม                  | should include 'บัตรส่งเสริม'                      | hits=1 titles=บัตรส่งเสริม                                                       |
| FAIL   | positive-35   | เอกสารยืนยันการได้รับสิทธิ    | should include 'บัตรส่งเสริม'                      | hits=12 titles=การส่งเสริมการลงทุน, สิทธิประโยชน์, ยกเว้นภาษีเงินได้นิติบุคคล... |
| PASS   | positive-36   | การเปิดดำเนินการ              | should include 'การเปิดดำเนินการ'                  | hits=4 titles=การเปิดดำเนินการ, การส่งเสริมการลงทุน, การโอนกิจการ, การรายงานผ... |
| PASS   | positive-37   | การตรวจสอบเงื่อนไขการผลิต     | should include 'การเปิดดำเนินการ'                  | hits=4 titles=การส่งเสริมการลงทุน, การโอนกิจการ, การเปิดดำเนินการ, การรายงานผ... |
| PASS   | positive-38   | อุตสาหกรรมเป้าหมาย            | should include 'อุตสาหกรรมเป้าหมาย'                | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | positive-39   | อุตสาหกรรมยุทธศาสตร์          | should include 'อุตสาหกรรมเป้าหมาย'                | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | positive-40   | S-Curve                       | should include 'อุตสาหกรรมเป้าหมาย'                | hits=1 titles=อุตสาหกรรมเป้าหมาย                                                 |
| PASS   | positive-41   | สูตรการผลิต                   | should include 'สูตรการผลิต'                       | hits=1 titles=สูตรการผลิต                                                        |
| FAIL   | positive-42   | อัตราการใช้ส่วนผสมวัตถุดิบ    | should include 'สูตรการผลิต'                       | hits=0 titles=<none>                                                             |
| PASS   | positive-43   | ผู้พำนักระยะยาว               | should include 'ผู้พำนักระยะยาว'                   | hits=1 titles=ผู้พำนักระยะยาว                                                    |
| PASS   | positive-44   | Long-Term Resident            | should include 'ผู้พำนักระยะยาว'                   | hits=1 titles=ผู้พำนักระยะยาว                                                    |
| PASS   | negative-01   | nonexistent-zzz-12345         | should return 0 hits                               | hits=0 titles=<none>                                                             |
+--------+---------------+-------------------------------+----------------------------------------------------+----------------------------------------------------------------------------------+
```
